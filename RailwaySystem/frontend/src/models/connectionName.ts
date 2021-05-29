import { TrainName } from './trainName';
import { CompanyName } from './companyName';

export interface ConnectionName {
    id: number;
    companyName: CompanyName;
    trainName: TrainName;
}