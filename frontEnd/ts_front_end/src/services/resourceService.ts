import axios, {AxiosError} from 'axios';
import type {RouteParamValue} from "vue-router";

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
            data.path = "/src/assets" + data.path;
            // console.log(data);
            return data
        } catch(err) {
            throw new Error(`获取课程资源失败：${(err as AxiosError).message}`)
        }
    }

}

export const resourceService = new ResourceService();