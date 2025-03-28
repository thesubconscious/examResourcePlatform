import axios, {AxiosError, type AxiosProgressEvent} from 'axios';
import type {RouteParamValue} from "vue-router";
import type {UnwrapRef} from "vue";

class ResourceService {
    private apiClient = axios.create({
        baseURL: import.meta.env.VITE_API_BASE,
        timeout: 5000,
        headers: { 'Content-Type': 'application/json' },
        withCredentials: true
    })

    async getResources(id: string | RouteParamValue[]){
        try {
            const { data } = await this.apiClient.get(`/courses/${id}/resources`)
            // console.log(data);
            return data
        } catch(err) {
            throw new Error(`获取课程资源失败：${(err as AxiosError).message}`)
        }
    }

    async getResourcesChapters(id: string | RouteParamValue[]){
        try {
            const { data } = await this.apiClient.get(`/courses/${id}/resources/chapters`)
            // console.log(data);
            return data
        } catch(err) {
            throw new Error(`获取课程资源失败：${(err as AxiosError).message}`)
        }
    }

    async createResource(
        course_id: string | RouteParamValue[],
        resource: Record<string, any>, // 任意JSON对象
        file: File,
        onProgress?: (percentage: number) => void
    ) {
        const formData = new FormData();
        formData.append('resource', new Blob(
            [JSON.stringify(resource)], {
                type: 'application/json'
            }));

        // 条件性添加文件
        if (file) {
            formData.append('file', file);
            const progressConfig = onProgress ? {
                onUploadProgress: (e: AxiosProgressEvent) => {
                    const percent = Math.round((e.loaded * 100) / (e.total || 1));
                    onProgress(percent);
                }
            } : {};

            return this.apiClient.post(
                `/courses/${course_id}/resources`,
                formData,
                {
                    headers: { 'Content-Type': 'multipart/form-data'  },
                    ...progressConfig
                });

        } else {
            return this.apiClient.post(
                `/courses/${course_id}/resources`,
                formData,
                {
                    headers: { 'Content-Type': 'multipart/form-data' }
                }
            );
        }
    }

    async deleteResource(id: string, course_id: string){
        try {
            const { data } = await this.apiClient.delete(`/courses/${course_id}/resources/${id}`)
            // console.log(data);
            return data
        } catch(err) {
            throw new Error(`删除资源失败：${(err as AxiosError).message}`)
        }
    }

}

export const resourceService = new ResourceService();