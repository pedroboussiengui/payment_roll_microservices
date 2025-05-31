
export class PayrollHttpGateway {
    private readonly baseUrl: string = 'http://localhost:8081/'

    async getAllEmployees(accessToken: string): Promise<Employee[]> {
        const response = await fetch(`${this.baseUrl}/employees`, {
            headers: {
                'Authorization': `${accessToken}`
            }
        })
        if (!response.ok) {
            let errorData;
            try {
                errorData = await response.json();
            } catch {
                errorData = { title: 'Unknown error', status: response.status };
            }
            throw {
                status: response.status,
                title: errorData.title,
                detail: errorData.detail
            };
        }

        return await response.json()
    }

    async listAll(userId: string) {
        const response = await fetch(`${this.baseUrl}/employees/${userId}/contracts`)
        const data =  await response.json()
        console.log(data);
        return data
    }
}

export type Employee = {
    id: string,
    name: string,
    document: string,
    birthDate: string
}