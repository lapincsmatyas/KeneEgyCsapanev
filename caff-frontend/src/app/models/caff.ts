import { Comment } from 'src/app/models/comment';

export class Caff {
  id: number;
  name: string;
  preview: string;
  comments: Comment[];
}
