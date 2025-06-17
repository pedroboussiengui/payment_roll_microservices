
export type UserContract = {
    id: string,
    matricula: string,
    organization: string,
    department: string
}

export type EmployeeContract = {
    id: string,
    matricula: string,
    entryDate: string,
    contractType: string,
    contractState: string,
    position: string,
    function: string,
    department: string,
    possibleEvents: string[]
}

export type Employee = {
    id: string,
    name: string,
    document: string,
    birthDate: string,
    identity: string,
    maritalStatus: string,
    gender: string,
    motherName: string,
    fatherName: string | null,
}

export type Organization = {
    id: string,
    name: string,
    cnpj: string,
    sigla: string,
    parentId: string | null,
    createdAt: string
}

interface BaseEvent {
    type: string;
    eventType: string;
    createdAt: string;
}

interface AdmissionEvent extends BaseEvent {
    eventType: "Admission";
    entryDate: string;
    exerciseDate: string;
}

interface AfastamentoEvent extends BaseEvent {
    eventType: "Afastamento";
    reason: string;
}

interface RetornoEvent extends BaseEvent {
    eventType: "Retorno";
    reason: string;
}

export type ContractEvent = AdmissionEvent | AfastamentoEvent | RetornoEvent;