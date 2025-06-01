// import type { Contract, Employee } from "$lib/utils/types";

// export class PayrollHttpGateway {
//     private readonly baseUrl: string = 'http://localhost:8081/'

//     async getAllEmployees(accessToken: string): Promise<Employee[]> {
//         const response = await fetch(`${this.baseUrl}/employees`, {
//             headers: {
//                 'Authorization': `Bearer ${accessToken}`
//             }
//         })
//         if (!response.ok) {
//             let errorData;
//             try {
//                 errorData = await response.json();
//             } catch {
//                 errorData = { title: 'Unknown error', status: response.status };
//             }
//             throw {
//                 status: response.status,
//                 title: errorData.title,
//                 detail: errorData.detail
//             };
//         }

//         return await response.json()
//     }

//     async listUserContracts(userId: string, token: string): Promise<Contract[]> {
//         const response = await fetch(`${this.baseUrl}/employees/${userId}/contracts`, {
//             headers: {
//                 'Authorization': `Bearer ${token}`,
//             }
//         }); 
//         return await response.json()
//     }
// }