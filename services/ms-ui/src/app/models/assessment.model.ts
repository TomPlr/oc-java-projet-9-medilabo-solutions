import {Note} from './note.model';
import {Gender} from './patient.model';

export type RiskLevel = "NONE" | "BORDERLINE" | "EARLY_ONSET" | "IN_DANGER";

export const assessmentResults: Map<RiskLevel, String> = new Map([
  ["NONE", "NO RISKS"],
  ["BORDERLINE", "BORDERLINE"],
  ["EARLY_ONSET", "EARLY ONSET"],
  ["IN_DANGER", "IN DANGER"]
]);


export interface AssessmentRequestData {
  dateOfBirth: string | undefined;
  gender: Gender | undefined;
  notes: Note[];
}

