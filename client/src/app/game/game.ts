export interface Game {
  gameId: string;
  prompts: string[];
  rounds: number;
  currentRound: number;
  currentPrompt: string;
  responses: string[];

}
