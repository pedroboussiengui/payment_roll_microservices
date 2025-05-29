
export class ContractHttpGateway {
    private readonly baseUrl: string = 'http://localhost:3002/contracts'

    async listAll() {
        const response = await fetch(this.baseUrl)
        const data =  await response.json()
        console.log(data);
        return data
    }
}