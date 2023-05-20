export class Diagnosis {
    id: number = 0;
    result: string = "";
    questions: Question[] = [];

    constructor(id: number) {
        this.id = id;
    }
}

export class Question {
    id: number = 0;
    content: string = ""; //pitanje
    target: string = ""; //target polja na backu koje azurira odgovor pitanja
    answers: string[] = []; //lista mogucih odgovora
    answered: boolean = false; // da li je na pitanje odgovoreno
    type: string = "" //result ili question
}