export type Contract = {
    id: string,
    matricula: string,
    entryDate: string,
    contractType: string,
    position: string,
    function: string,
    department: string,
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