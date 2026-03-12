/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum PermissionLevel implements StringRepresentable {
/*    */   public static final Codec<PermissionLevel> CODEC;
/* 11 */   ALL("all", 0),
/* 12 */   MODERATORS("moderators", 1),
/* 13 */   GAMEMASTERS("gamemasters", 2),
/* 14 */   ADMINS("admins", 3),
/* 15 */   OWNERS("owners", 4); private static final IntFunction<PermissionLevel> BY_ID;
/*    */   
/*    */   static  {
/* 18 */     CODEC = StringRepresentable.fromEnum(PermissionLevel::values);
/*    */     
/* 20 */     BY_ID = ByIdMap.continuous(level -> level.id, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 21 */     Objects.requireNonNull(BY_ID); INT_CODEC = Codec.INT.xmap(BY_ID::apply, level -> Integer.valueOf(level.id));
/*    */   }
/*    */   
/*    */   public static final Codec<PermissionLevel> INT_CODEC;
/*    */   
/*    */   PermissionLevel(String name, int id) {
/* 27 */     this.name = name;
/* 28 */     this.id = id;
/*    */   }
/*    */   private final String name; private final int id;
/*    */   
/* 32 */   public boolean isEqualOrHigherThan(PermissionLevel other) { return (this.id >= other.id); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static PermissionLevel byId(int level) { return (PermissionLevel)BY_ID.apply(level); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */