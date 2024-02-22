import {Exception} from "../model/Exception";

export class Service<T> {

    private arr: T[];

    constructor(arr:T[] = []) {
        this.arr = arr;
    }

    async findAll(check: boolean): Promise<T[] | Exception> {
        if(check) {
            return Promise.resolve({message:'Something went wrong...'});
        }
        return Promise.resolve(this.arr);
    }
}