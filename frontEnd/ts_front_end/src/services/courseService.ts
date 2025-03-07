import axios, {AxiosError} from 'axios';
import type {RouteParamValue} from "vue-router";

class CourseService {
    private apiClient = axios.create({
        baseURL: import.meta.env.VITE_API_BASE,
        timeout: 5000,
        headers: { 'Content-Type': 'application/json' },
        withCredentials: true
    })

    async getAllCourses(){
        try {
            const { data } = await this.apiClient.get(`/courses`)
            console.debug('课程:', data)
            return data
        } catch(err) {
            throw new Error(`获取课程失败：${(err as AxiosError).message}`)
        }
    }

    async getCoursesDetails(id: string | RouteParamValue[]){
        try {
            const { data } = await this.apiClient.get(`/courses/${id}`)
            return data
        } catch(err) {
            throw new Error(`获取课程失败：${(err as AxiosError).message}`)
        }
    }

    async createCourse(name:string, introduction:string, teacher_id:string){
        try {
            const { data } = await this.apiClient.post('/courses', {
                name, introduction, teacher_id
            })
            return data
        } catch(err) {
            throw new Error(`注册失败：${(err as AxiosError).message}`)
        }
    }

}

export const courseService = new CourseService();