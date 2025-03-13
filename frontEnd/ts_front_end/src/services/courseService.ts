import axios, {AxiosError, type AxiosProgressEvent} from 'axios';
import type {RouteParamValue} from "vue-router";

class CourseService {
    private apiClient = axios.create({
        baseURL: import.meta.env.VITE_API_BASE,
        timeout: 5000,
        headers: {'Content-Type': 'application/json'},
        withCredentials: true
    })

    async getAllCourses() {
        try {
            const {data} = await this.apiClient.get(`/courses`)
            console.debug('课程:', data)
            return data
        } catch (err) {
            throw new Error(`获取课程失败：${(err as AxiosError).message}`)
        }
    }

    async getCoursesDetails(id: string | RouteParamValue[]) {
        try {
            const {data} = await this.apiClient.get(`/courses/${id}`)
            data.img_path = "/src/assets" + data.img_path;
            // console.log(data);
            return data
        } catch (err) {
            throw new Error(`获取课程失败：${(err as AxiosError).message}`)
        }
    }

    async createCourse(name: string, introduction: string, teacher_id: string) {
        try {
            const {data} = await this.apiClient.post('/courses', {
                name, introduction, teacher_id
            })
            return data
        } catch (err) {
            throw new Error(`注册失败：${(err as AxiosError).message}`)
        }
    }

    // async updateCourse(course: Course, file: File) {
    //     const formData = new FormData();
    //     formData.append('course', new Blob([JSON.stringify(course)], {
    //         type: "application/json"
    //     }));
    //
    //     if (file) {
    //         formData.append('file', file);
    //     }
    //
    //     try {
    //         const {data} = await this.apiClient.patch(`/courses/${course.course_id}`, formData, {
    //             headers: {
    //                 'Content-Type': 'multipart/form-data' // 显式设置
    //             }
    //         });
    //         return data;
    //     } catch (err) {
    //         throw new Error(`更新失败：${(err as AxiosError).message}`);
    //     }
    // }

    async updateCourse(
        course: Record<string, any>, // 任意JSON对象
        file?: File, // 明确标记为可选参数
        onProgress?: (percentage: number) => void
    ) {
        const formData = new FormData();

        // console.log(course)

        formData.append('course', new Blob(
            [JSON.stringify(course)], {
            type: 'application/json'
        }));

        // 条件性添加文件
        if (file) {
            formData.append('file', file);
            // 当有文件时才启用进度监听
            const progressConfig = onProgress ? {
                onUploadProgress: (e: AxiosProgressEvent) => {
                    const percent = Math.round((e.loaded * 100) / (e.total || 1));
                    onProgress(percent);
                }
            } : {};

            return this.apiClient.patch(
                `/courses/${course.course_id}`,
                formData,
                {
                    headers: { 'Content-Type': 'multipart/form-data' },
                    ...progressConfig
                }
            );
        } else {
            // 无文件时使用普通JSON格式
            return this.apiClient.patch(
                `/courses/${course.course_id}`,
                JSON.stringify(course),
                { headers: { 'Content-Type': 'application/json' } }
            );
        }
    }

}
export const courseService = new CourseService();