export interface DiagnosisEntry {
    startedAt: string;
    id: number;
    diagnosisResult: {content: string},
    symptoms: any;
}

export class Diagnosis {
    id: number = 0;
    result: string = "";
    questions: Question[] = [];
    type: DiagnosisType = DiagnosisType.NONE;

    constructor(id: number) {
        this.id = id;
    }

    updateQuestion = (question: Question) => {
        let index = this.questions.findIndex(q => q.content === question.content);
        this.questions[index] = question;
    }
}

export class Question {
    diagnosisId: number = 0;
    content: string = ""; //pitanje
    target: string = ""; //target polja na backu koje azurira odgovor pitanja
    answers: string[] = []; //lista mogucih odgovora
    answered: string = ''; // da li je na pitanje odgovoreno
    type: QuestionType = QuestionType.MULTIPLE 
    responseType: ResponseType = ResponseType.QUESTION

    constructor(content: string, answers: string[], answered: string) {
        this.content = content;
        this.answers = answers;
        this.answered = answered;
    }
}

export enum DiagnosisType {
    NONE = '',
    INDUSTRY = 'INDUSTRY',
    CONTROLLED_SUBSTANCES = 'CONTROLLED_SUBSTANCES',
}

export enum QuestionType {
    BOOL = 'bool',
    MULTIPLE = 'multiple',
    GASNA_HROMATOGRAFIJA = 'gasna-hromatografija',
    SPEKTROFOTOMETRIJA = 'spektofotometrija'
}

export enum ResponseType {
    RESULT = 'RESULT',
    QUESTION = 'QUESTION'
}