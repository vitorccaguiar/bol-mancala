import { Match } from './match';

export class InputMessage {
    type: string;
    playerId: string;
    fingerprint: string;
    match: Match;
}
