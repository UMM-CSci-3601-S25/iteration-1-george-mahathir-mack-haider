export interface Game {
  players: string[];
  prompts: string[];
  rounds: number;
  currentRound: number;
  currentPrompt: string;
  responses: string[];

}
