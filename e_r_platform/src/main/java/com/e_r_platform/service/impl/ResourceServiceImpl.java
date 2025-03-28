package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.config.WebConfig;
import com.e_r_platform.model.Resource;
import com.e_r_platform.service.ResourceService;
import com.e_r_platform.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
* @author Administrator
* @description 针对表【resource】的数据库操作Service实现
* @createDate 2024-04-27 15:52:20
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private WebConfig webConfig;

    public Resource createResource(Resource resource, MultipartFile file) throws IOException {

        if (resource.getType() == Resource.NodeType.LEAF) {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("叶子节点必须上传文件");

            }
            uploadFile(resource, file);
        }

        // 自动填充显示顺序
        int maxOrder = 0;
        if (resource.getType() == Resource.NodeType.LEAF)
            maxOrder = resourceMapper.selectMaxOrderForLeaf(resource.getParent_node_id());
        else if (resource.getType() == Resource.NodeType.CHAPTER)
            maxOrder = resourceMapper.selectMaxOrderForChapter(resource.getCourse_id());
        resource.setDisplay_order(maxOrder != 0 ? maxOrder + 1 : 1);
        resourceMapper.insertResource(resource);
        return resource;
    }

    private Path uploadFile(Resource resource, MultipartFile file) throws IOException {
        // 安全校验
        String fileName = file.getOriginalFilename();
        if (fileName.contains(";")) {
            throw new IllegalArgumentException("文件名包含非法字符：;");
        }

        // 动态构建目录结构
        List<String> pathSegments = new ArrayList<>();
        pathSegments.add("course");
        pathSegments.add(resource.getCourse_id().toString());

        // 递归查找章节节点
        Resource current = getParentChapterNode(resource.getParent_node_id());
        if (current != null) {
            pathSegments.add("chapter");
            pathSegments.add(current.getNode_id().toString());
            buildNodePath(current.getNode_id(), pathSegments);
        }

        // 添加当前节点空间
//        pathSegments.add("temp_"+resource.getNode_id()); // 先占位后续替换

        // 组装完整路径
        String relativePath =  String.join("/", pathSegments)
                + "/" + fileName;
        Path storagePath = Paths.get(webConfig.uploadBasePath, relativePath);

        // 确保目录存在
        Files.createDirectories(storagePath.getParent());

        // 转存文件并设置URI
        file.transferTo(storagePath);
        resource.setFile_uri(relativePath);

        return storagePath;
    }
    // 递归构建子节点路径
    private void buildNodePath(Integer parentId, List<String> pathSegments) {
        Resource parent = resourceMapper.selectById(parentId);
        if (parent == null || parent.getType() == Resource.NodeType.CHAPTER) return;

        pathSegments.add("node");
        pathSegments.add(parent.getNode_id().toString());

        if (parent.getParent_node_id() != null) {
            buildNodePath(parent.getParent_node_id(), pathSegments);
        }
    }
    // 获取最近的章节父节点
    private Resource getParentChapterNode(Integer parentId) {
        while (parentId != null) {
            Resource parent = resourceMapper.selectById(parentId);
            if (parent.getType() == Resource.NodeType.CHAPTER) {
                return parent;
            }
            parentId = parent.getParent_node_id();
        }
        return null;
    }

    public int updateResource(Resource resource, MultipartFile newFile) throws IOException {
        Resource existing = getById(resource.getNode_id());

        // 强制校验不可变字段
        if (!existing.getCourse_id().equals(resource.getCourse_id())
                || !existing.getParent_node_id().equals(resource.getParent_node_id())
                || existing.getType() != resource.getType()) {
            return -1;
        }

        // 叶子节点文件更新逻辑
        if (resource.getType() == Resource.NodeType.LEAF && newFile != null) {
            Path oldPath = Paths.get(webConfig.uploadBasePath, existing.getFile_uri());
            Path newPath = uploadFile(resource, newFile);

            Files.createDirectories(newPath.getParent());
            newFile.transferTo(newPath);
            resource.setFile_uri(String.valueOf(Paths.get(webConfig.uploadBasePath).relativize(newPath)));

            // 异步删除旧文件
            CompletableFuture.runAsync(() -> {
                try { Files.deleteIfExists(oldPath); }
                catch (IOException e) { log.error("旧文件删除失败", e); }
            });
        }

        resourceMapper.updateResource(resource);
        return resource.getNode_id();
    }

    public void deleteResource(int nodeId) throws IOException {
        Resource target = resourceMapper.selectById(nodeId);
        if (target == null) {
            throw new NoSuchElementException("资源节点不存在: " + nodeId);
        }

        // 递归处理子节点
        if (target.getType() == Resource.NodeType.CHAPTER) {
            List<Resource> children = resourceMapper.selectChildren(nodeId);
            for (Resource child : children) {
                deleteResource(child.getNode_id());
            }
        }

        // 物理删除文件
        deleteFile(target);

        // 调整兄弟节点的display_order
        resourceMapper.decrementOrderAfterDelete(
                target.getParent_node_id(),
                target.getDisplay_order()
        );

        // 最后删除自身
        resourceMapper.deleteById(nodeId);
    }
    private void deleteFile(Resource target) throws IOException {
        if (target.getType() == Resource.NodeType.LEAF && target.getFile_uri() != null) {
            Path filePath = Paths.get(webConfig.uploadBasePath, target.getFile_uri().replaceFirst("/uploads/", ""));
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                // 清理空目录
                Path dir = filePath.getParent();
                while (dir != null && !dir.equals(Paths.get(webConfig.uploadBasePath))) {
                    if (Files.list(dir).findAny().isEmpty()) {
                        Files.delete(dir);
                        dir = dir.getParent();
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public Resource getById(Integer nodeId) {
//        Resource resource = resourceMapper.selectById(nodeId);
        return resourceMapper.selectById(nodeId);
    }

    public File downloadFile(int nodeId) throws FileNotFoundException, IllegalArgumentException {
        // 查询资源信息
        Resource resource = getById(nodeId);
        if (resource == null || !Resource.NodeType.LEAF.equals(resource.getType())) {
            throw new IllegalArgumentException("Invalid resource ID or not a leaf node.");
        }

        String fileUri = resource.getFile_uri();
        if (fileUri == null || fileUri.isEmpty()) {
            throw new FileNotFoundException("File URI is empty for the given node ID.");
        }

        // 假设文件存储在本地路径中，这里直接从URI创建文件对象。
        File file = new File(webConfig.uploadBasePath, fileUri);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("The requested file does not exist.");
        }

        return file;
    }

    public List<Resource> getChildren(Integer parentId) {
        return parentId == null ?
                Collections.emptyList() :
                resourceMapper.selectChildren(parentId);
    }

    public List<Resource> getAll(Integer courseId){
        return courseId == null ?
                Collections.emptyList() :
                resourceMapper.selectAll(courseId);
    }

    public List<Resource> getAllChapters(Integer courseId){
        return courseId == null ?
                Collections.emptyList() :
                resourceMapper.selectChapters(courseId);
    }

}




