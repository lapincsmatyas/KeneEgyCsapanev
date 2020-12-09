import { Comment } from 'src/app/models/comment';
import {SafeUrl} from "@angular/platform-browser";

export class Caff {
  id: number;
  name: string;
  preview: SafeUrl;
  comments: Comment[];
}
