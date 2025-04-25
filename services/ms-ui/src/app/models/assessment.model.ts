import {Note} from './note.model';

export type RiskLevel = "NONE" | "BORDERLINE" | "EARLY_ONSET" | "IN_DANGER";

export const assessmentResults: Map<RiskLevel, String> = new Map([
  ["NONE", "NO RISKS"],
  ["BORDERLINE", "BORDERLINE"],
  ["EARLY_ONSET", "EARLY ONSET"],
  ["IN_DANGER", "IN DANGER"]
]);


export interface AssessmentRequestData {
  dateOfBirth: string;
  gender: string;
  notes: Note[];
}

