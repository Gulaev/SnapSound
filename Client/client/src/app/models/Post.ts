import {Comment} from './Comment'

export interface Post {
    id? : number
    title : string
    caption : string
    location : string
    username? : string
    likes? : number
    image? : File
    userLikes? : string[]
    comments? : Comment[]
    
}