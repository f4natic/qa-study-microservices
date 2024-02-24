import {Exception} from "../model/Exception";
import axios, {AxiosResponse} from "axios";

export class Service<T> {

    private url: string;

    constructor(url: string) {
        this.url = url;
    }

    async findAll(check: boolean): Promise<T[] | Exception> {
        try {
            const response: AxiosResponse<T[]> = await axios.get(this.url);
            return response.data;
        } catch (error: any) {
            return {message: `Couldn't connect to server. Try again...`};
        }
    }
}