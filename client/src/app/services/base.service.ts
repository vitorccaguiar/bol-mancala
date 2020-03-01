export class BaseService {
  getHeaders(): any {
    return {
      responseType: 'application/json',
    };
  }
}