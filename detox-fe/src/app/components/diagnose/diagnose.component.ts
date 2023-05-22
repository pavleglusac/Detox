import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faUser, faX, faRepeat } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { Diagnosis, DiagnosisType, Question, QuestionType, ResponseType } from 'src/app/model/diagnose';
import { DiagnoseService } from 'src/app/services/diagnose.service';
import { DiagnosisStateService } from 'src/app/services/store/diagnosis-state.service';
import { AnswersPipe } from 'src/app/shared/pipes/answers.pipe';


@Component({
  selector: 'app-diagnose',
  templateUrl: './diagnose.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, FontAwesomeModule, AnswersPipe],
})
export class DiagnoseComponent{
  @ViewChild('scrollMarker') private scrollMarker!: ElementRef;
  faUserEmail: IconDefinition = faUser;
  faEnd: IconDefinition = faX;
  faReset: IconDefinition = faRepeat;

  diagnosis: Diagnosis | null = null;
  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
    ])})
  questionAnswer: string = '';

  constructor(private diagnoseService: DiagnoseService, private toastr: ToastrService, private diagnosisStateService: DiagnosisStateService) { 
    diagnosisStateService.getDiagnosisState().subscribe((diagnosis: Diagnosis | null) => {
      this.diagnosis = diagnosis;
    });
  }

  onSubmit = () => {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    };
    this.diagnoseService.startDiagnosis(
      this.email!.value!,
      (value: any) => {
        this.diagnosis = new Diagnosis(value);
        this.diagnosis.questions.push(new Question("Da li pacijent radi u industriji?", ["Da", "Ne"], ''));
        this.diagnosisStateService.setDiagnosisState(this.diagnosis);
      },
      (error: any) => {
        this.diagnosis = null;
        //ako je dijagnoza u procesu treba da se dobavi i postavi
        this.toastr.error(error.message);
      }
    );
  }

  answerQuestion = (question: Question) => {
    if (question.type !== QuestionType.GASNA_HROMATOGRAFIJA && question.type !== QuestionType.SPEKTROFOTOMETRIJA  && this.questionAnswer === '') {
      this.toastr.error('Morate odgovoriti na pitanje');
      return;
    }
    if (this.diagnosis?.questions.length === 1) {
      this.getFirstAnswer(question);
    } else {
      this.getRestOfTheAnswers(question);
    }
  }

  getFirstAnswer(question: Question) {
    this.diagnosis!.type = this.questionAnswer === 'Da' ? DiagnosisType.INDUSTRY : DiagnosisType.CONTROLLED_SUBSTANCES;
      this.diagnoseService.setSymptoms(this.diagnosis!.id, this.diagnosis!.type, 
        (newQuestion)=>{this.updateDiagnosis(question, newQuestion)},
        (error)=>{this.toastr.error(error.message);}
      );
  }

  getRestOfTheAnswers(question: Question) {
    this.diagnosis?.type === DiagnosisType.INDUSTRY 
    ? this.diagnoseService.addIndustySymptom(this.diagnosis!.id, [{op: "replace", path: question.target, "value": this.questionAnswer}],
      (newQuestion)=> { this.updateDiagnosis(question, newQuestion);}, 
      (error)=>{this.toastr.error(error.message);}
      ) 
    : this.diagnoseService.addControlledSubstancesSymptom(this.diagnosis!.id, [{op: "replace", path: question.target, "value": this.questionAnswer}], 
      (newQuestion)=> { this.updateDiagnosis(question, newQuestion);}, 
      (error)=>{this.toastr.error(error.message);} 
      );
  }

  updateDiagnosis(question: Question, newQuestion: Question): void {
    question.answered = this.questionAnswer;
    this.diagnosis!.updateQuestion(question);
    this.diagnosis!.questions.push(newQuestion);
    this.diagnosisStateService.setDiagnosisState(this.diagnosis!);
    this.questionAnswer = '';
    this.scrollMarker.nativeElement.scrollIntoView({ behavior: 'smooth' });
    if (newQuestion.responseType === ResponseType.RESULT || !newQuestion.answers) {
      this.toastr.success(newQuestion.content, 'Rezultat');
    }
  }

  startAgain = () => {
    this.questionAnswer = '';
    this.diagnosis = null;
    this.loginForm.reset();
    this.diagnosisStateService.clearDiagnosisState();
  }

  get email() {
    return this.loginForm.get('email');
  }

  get answeredQuestions() {
    return this.diagnosis?.questions.filter(question => question.responseType === 'QUESTION' && question.answered !== '');
  }

  get unansweredQuestions() {
    return this.diagnosis?.questions.filter(question => question.responseType === 'QUESTION' && question.answered === '');
  }

  get result() {
    return this.diagnosis?.questions.find(question => question.responseType === ResponseType.RESULT);
  }

}
