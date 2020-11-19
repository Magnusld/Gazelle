import { LocalDate } from "@/client/date";

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
  user: UserResponse;
  token: string;
}

export interface UserResponse {
  id: number;
  firstname: string;
  lastname: string;
}

export interface NewCourseRequest {
  name: string;
}

export interface CourseResponse {
  id: number;
  name: string;
  isOwner: boolean | null;
  isFollower: boolean | null;
  currentPost: PostResponse | null;
  nextPost: PostResponse | null;
  previousPost: PostResponse | null;
}

export interface CourseContentResponse {
  id: number;
  name: string;
  isOwner: boolean | null;
  isFollower: boolean | null;
  posts: PostResponse[];
}

export interface NewPostRequest {
  title: string;
  description: string;
  startDate: LocalDate;
  endDate: LocalDate;
  chores: NewChoreRequest[];
}

export interface PostResponse {
  id: number;
  title: string;
  description: string;
  startDate: LocalDate;
  endDate: LocalDate;
  choresDone: number | null;
  choresFocused: number | null;
  choresCount: number;
}

export interface PostContentResponse {
  id: number;
  title: string;
  description: string;
  startDate: LocalDate;
  endDate: LocalDate;
  courseId: number;
  courseName: string;
  isOwning: boolean | null;
  chores: ChoreResponse[];
}

export interface NewChoreRequest {
  id: number | null;
  key: number;
  text: string;
  dueDate: LocalDate | null;
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
  dueDate: LocalDate | null;
  progress: ChoreProgress | null;
}
