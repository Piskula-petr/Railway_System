import { Company } from "./company";
import { TrainName } from "./trainName";

export interface Connection {
    id: number;
    company: Company;
    trainName: TrainName;
}