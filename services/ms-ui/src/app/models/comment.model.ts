export interface Comment {
  id?: number;
  patientId: number;
  date?: Date;
  content: string;
  createdBy: string;
} 