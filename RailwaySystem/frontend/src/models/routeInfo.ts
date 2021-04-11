import { Connection } from "./connection";
import { Route } from "./route";

export interface RouteInfo {
    connection: Connection;
    startRoute: Route;
    endRoute: Route;
}