import { ChoreFullResponse, ChoreProgress } from "@/client/types";
import restClient from "@/client/restClient";

export async function setChoreState(
  choreId: number,
  userId: number,
  value: ChoreProgress
): Promise<void> {
  return await restClient.put(`/users/${userId}/chores/${choreId}/progress`, {
    value
  });
}

export async function getFocusedChores(
  userId: number
): Promise<ChoreFullResponse[]> {
  return await restClient.get(`/users/${userId}/focusedChores/`);
}
