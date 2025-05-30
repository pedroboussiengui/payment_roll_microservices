
export class PayrollHttpGateway {
    private readonly baseUrl: string = 'http://localhost:8081/'

    async getAllEmployees() {
        const response = await fetch(`${this.baseUrl}/employees`)
        return await response.json()
    }
}

export type Employee = {
    id: string,
    name: string,
    document: string,
    birthDate: string
}