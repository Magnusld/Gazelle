export type LocalDate = [number, number, number];

export function localDateOfDate(date: Date): LocalDate {
  const nums = date
    .toISOString()
    .split("T")[0]
    .split("-")
    .map(Number);
  return [nums[0], nums[1], nums[2]];
}

export function dateOfLocalDate(localDate: LocalDate): Date {
  return new Date(
    `${localDate[0]}-${localDate[1]}-${localDate[2]}T23:00:00.000Z`
  );
}

export function daysUntil(localDate: LocalDate): number {
  return Math.floor(
    (dateOfLocalDate(localDate).getTime() - new Date().getTime()) / 86400000
  );
}
