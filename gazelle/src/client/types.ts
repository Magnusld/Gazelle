import { User } from "@/types";

export interface LogInRequest {
  email: string;
  password: string;
}

export interface SignUpRequest {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
}

export interface LogInResponse {
  user: User;
  token: string;
}

export interface UserResponse {
  id: number;
  firstname: string;
  lastname: string;
}

export interface NewChoreRequest {
  id: number;
  key: number;
  text: string;
  dueDate: Date;
}

export interface NewPostRequest {
  title: string;
  description: string;
  startDate: Date;
  endDate: Date;
  chores: NewChoreRequest[];
}

export interface PostResponse {
  id: number;
  title: string;
  startDate: Date;
  endDate: Date;
  choresDone: number;
  choresFocused: number;
  choresCount: number;
}

export interface PostContentResponse {
  id: number;
  title: string;
  description: string;
  startDate: Date;
  endDate: Date;
  courseId: number;
  chores: ChoreResponse;
}

export enum ChoreProgress {
  UNDONE = "undone",
  FOCUSED = "focused",
  DONE = "done"
}

export interface ChoreResponse {
  id: number;
  key: number;
  text: string;
  dueDate: Date;
  progress: ChoreProgress;
}

export interface NewCourseRequest {
  name: string;
}

export interface CourseResponse {
  id: number;
  name: string;
  isOwner: boolean;
  isFollower: boolean;
  currentPost?: PostResponse;
  nextPost?: PostResponse;
}
