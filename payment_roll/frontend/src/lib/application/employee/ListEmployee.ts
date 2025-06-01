// import { PayrollHttpGateway } from "$lib/infra/http/PayrollHttpClient";
// import type { Contract, Employee } from "$lib/utils/types";

// export class ListEmployee {
//     constructor(
//         private readonly payroll = new PayrollHttpGateway(),
//     ) {}

//     async execute(accessToken: string): Promise<Employee[]> {
//         return this.payroll.getAllEmployees(accessToken);
//     }
// }

// export class ListUserContracts {
//     constructor(
//         private readonly payroll = new PayrollHttpGateway(),
//     ) {}

//     async execute(userId: string, token: string): Promise<Contract[]> {
//         return this.payroll.listUserContracts(userId, token);
//     }
// }