import { User } from './user';

export class Match {
  id: string;
  firstPlayer: User;
  secondPlayer: User;
  status: string;
  firstPlayerPits: number[];
  secondPlayerPits: number[];
  playerTurn: User;
  winner: string;
  tie: boolean;
}
