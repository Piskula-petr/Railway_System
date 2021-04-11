import moment from "moment";

/**
 * Formát času
 * 
 * @param value - vstupní hodnota
 */
function timeFormat(value: string): string {

    const time = moment(value, "HH:mm:ss");

    return moment(time).format("HH:mm");
}

export { timeFormat as TimeFormat }