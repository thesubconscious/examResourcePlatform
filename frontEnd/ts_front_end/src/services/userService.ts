import axios from 'axios';

export class UserService{
    private baseUrl :string = "http://localhost:8080/users"

    async login(email: string, password: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/login', {
                email,
                password
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }

    async register(email: string, name: string, password: string, identity: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/register', {
                email,
                name,
                password,
                identity
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }
}