import { ChoreProgress } from "@/client/types";
import restClient from "@/client/restClient";

export async function setChoreState(
  choreId: number,
  userId: number,
  value: ChoreProgress
): Promise<void> {
  return await restClient.put(
    "/users/" + userId + "/chores/" + choreId + "/progress",
    { value }
  );
}
