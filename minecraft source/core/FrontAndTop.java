/*    */ package net.minecraft.core;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public static enum FrontAndTop implements StringRepresentable {
/*  7 */   DOWN_EAST("down_east", Direction.DOWN, Direction.EAST),
/*  8 */   DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH),
/*  9 */   DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),
/* 10 */   DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),
/*    */   
/* 12 */   UP_EAST("up_east", Direction.UP, Direction.EAST),
/* 13 */   UP_NORTH("up_north", Direction.UP, Direction.NORTH),
/* 14 */   UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
/* 15 */   UP_WEST("up_west", Direction.UP, Direction.WEST),
/*    */   
/* 17 */   WEST_UP("west_up", Direction.WEST, Direction.UP),
/* 18 */   EAST_UP("east_up", Direction.EAST, Direction.UP),
/* 19 */   NORTH_UP("north_up", Direction.NORTH, Direction.UP),
/* 20 */   SOUTH_UP("south_up", Direction.SOUTH, Direction.UP);
/*    */   
/*    */   static  {
/* 23 */     NUM_DIRECTIONS = Direction.values().length;
/* 24 */     BY_TOP_FRONT = (FrontAndTop[])Util.make(new FrontAndTop[NUM_DIRECTIONS * NUM_DIRECTIONS], map -> {
/*    */ 
/*    */           
/* 27 */           for (FrontAndTop value : values())
/* 28 */             map[lookupKey(value.front, value.top)] = value; 
/*    */         });
/*    */   }
/*    */   
/*    */   private static final int NUM_DIRECTIONS;
/*    */   private static final FrontAndTop[] BY_TOP_FRONT;
/*    */   private final String name;
/*    */   private final Direction top;
/*    */   private final Direction front;
/*    */   
/* 38 */   private static int lookupKey(Direction front, Direction top) { return front.ordinal() * NUM_DIRECTIONS + top.ordinal(); }
/*    */ 
/*    */   
/*    */   FrontAndTop(String name, Direction front, Direction top) {
/* 42 */     this.name = name;
/* 43 */     this.front = front;
/* 44 */     this.top = top;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static FrontAndTop fromFrontAndTop(Direction front, Direction top) { return BY_TOP_FRONT[lookupKey(front, top)]; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public Direction front() { return this.front; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Direction top() { return this.top; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\FrontAndTop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */