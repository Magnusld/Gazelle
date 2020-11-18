export type LocalDate = string;

export function localDateOfDate(date: Date) {
  return date.toISOString().split("T")[0];
}

export function dateOfLocalDate(localDate: LocalDate) {
  return localDate + "T23:00:00.000Z";
}
