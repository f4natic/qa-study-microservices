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

    async getTotal(): Promise<number | string> {
        try {
            const response: AxiosResponse<number> = await axios.get(this.url);
            console.log(response.data);
            return response.data;
        } catch (error: any) {
            return `Couldn't connect to server`;
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
}