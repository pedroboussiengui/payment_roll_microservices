
// export type SessionTokens = {
//     sessionId: string,
//     accessToken: string,
//     refreshToken: string
// }

export type SessionTokens = {
    sessionId: string,
    accessToken: string
}

export type Employee = {
    id: string,
    name: string,
    document: string,
    birthDate: string
}

export type Contract = {
    id: string,
    matricula: string,
    entryDate: string,
    contractType: string,
    position: string,
    function: string,
    department: string,
}