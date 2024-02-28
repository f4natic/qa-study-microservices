import {Exception} from "../model/Exception";
import axios, {AxiosResponse} from "axios";

export interface Page<T> {
    content: T[]; // Массив элементов на текущей странице
    totalPages: number; // Общее количество страниц
    totalElements: number; // Общее количество элементов
    currentPage: number; // Номер текущей страницы
    pageSize: number; // Количество элементов на странице
    hasNextPage: boolean; // Флаг, указывающий, есть ли следующая страница
    hasPreviousPage: boolean; // Флаг, указывающий, есть ли предыдущая страница
}

export class Service<T> {

    private url: string;

    constructor(url: string) {
        this.url = url;
    }

    async getTotal(): Promise<number | Exception> {
        try {
            const response: AxiosResponse<number> = await axios.get(`${this.url}/total`);
            console.log(response.data);
            return response.data;
        } catch (error: any) {
            return {message: error.response.data.message};
        }
    }

    async findAll(page: number, pageSize: number): Promise<Page<T> | Exception> {
        try {
            const response: AxiosResponse<Page<T> > = await axios.get(this.url, {
                params: {
                    page: page,
                    pageSize: pageSize,
                }
            });
            return response.data;
        } catch (error: any) {
            return {message: `Couldn't connect to server. Try again...`};
            // return {message: error.response.data.message};
        }
    }

    async save(t: T): Promise<T | Exception> {
        try {
            const response: AxiosResponse<T> = await axios.post(`${this.url}/create`, t);
            return response.data;
        }catch (error: any) {
            return {message: error.response.data.message};
        }
    }

    async update(name: string, t: T): Promise<T | Exception> {
        try {
            const response: AxiosResponse<T> = await axios.put(`${this.url}/${name}`, t);
            return response.data;
        }catch (error: any) {
            return {message: error.response.data.message};
        }
    }

    async delete(name: string): Promise<void | Exception> {
        try {
            await axios.delete(`${this.url}/${name}`);
        }catch (error: any) {
            return {message: error.response.data.message};
        }
    }
}