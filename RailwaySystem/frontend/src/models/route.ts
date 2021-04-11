import { StationName } from "./stationName";

export interface Route {
    id: number;
    stationName: StationName;
    arrival: string;
    departure: string;
    stationOrder: number;
    distance: number;
}