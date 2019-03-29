export interface IResponseChoice {
  id?: number;
  text?: string;
  questionId?: number;
}

export const defaultValue: Readonly<IResponseChoice> = {};
