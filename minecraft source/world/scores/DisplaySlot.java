/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DisplaySlot
/*    */   implements StringRepresentable
/*    */ {
/* 11 */   LIST(0, "list"),
/* 12 */   SIDEBAR(1, "sidebar"),
/* 13 */   BELOW_NAME(2, "below_name"),
/* 14 */   TEAM_BLACK(3, "sidebar.team.black"),
/* 15 */   TEAM_DARK_BLUE(4, "sidebar.team.dark_blue"),
/* 16 */   TEAM_DARK_GREEN(5, "sidebar.team.dark_green"),
/* 17 */   TEAM_DARK_AQUA(6, "sidebar.team.dark_aqua"),
/* 18 */   TEAM_DARK_RED(7, "sidebar.team.dark_red"),
/* 19 */   TEAM_DARK_PURPLE(8, "sidebar.team.dark_purple"),
/* 20 */   TEAM_GOLD(9, "sidebar.team.gold"),
/* 21 */   TEAM_GRAY(10, "sidebar.team.gray"),
/* 22 */   TEAM_DARK_GRAY(11, "sidebar.team.dark_gray"),
/* 23 */   TEAM_BLUE(12, "sidebar.team.blue"),
/* 24 */   TEAM_GREEN(13, "sidebar.team.green"),
/* 25 */   TEAM_AQUA(14, "sidebar.team.aqua"),
/* 26 */   TEAM_RED(15, "sidebar.team.red"),
/* 27 */   TEAM_LIGHT_PURPLE(16, "sidebar.team.light_purple"),
/* 28 */   TEAM_YELLOW(17, "sidebar.team.yellow"),
/* 29 */   TEAM_WHITE(18, "sidebar.team.white"); public static final StringRepresentable.EnumCodec<DisplaySlot> CODEC; public static final IntFunction<DisplaySlot> BY_ID; private final int id; private final String name;
/*    */   
/*    */   static  {
/* 32 */     CODEC = StringRepresentable.fromEnum(DisplaySlot::values);
/* 33 */     BY_ID = ByIdMap.continuous(DisplaySlot::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   DisplaySlot(int id, String name) {
/* 39 */     this.id = id;
/* 40 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/* 44 */   public int id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */   
/*    */   public static DisplaySlot teamColorToSlot(ChatFormatting color) {
/* 53 */     switch (color) { default: throw new MatchException(null, null);case BLACK: case DARK_BLUE: case DARK_GREEN: case DARK_AQUA: case DARK_RED: case DARK_PURPLE: case GOLD: case GRAY: case DARK_GRAY: case BLUE: case GREEN: case AQUA: case RED: case LIGHT_PURPLE: case YELLOW: case WHITE: case BOLD: case ITALIC: case UNDERLINE: case RESET: case OBFUSCATED: case STRIKETHROUGH: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 70 */       null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\DisplaySlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */