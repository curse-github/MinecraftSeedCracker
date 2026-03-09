/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
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
/*    */ public class AlwaysPass
/*    */   implements PermissionCheck
/*    */ {
/* 21 */   public static final AlwaysPass INSTANCE = new AlwaysPass();
/*    */   
/* 23 */   public static final MapCodec<AlwaysPass> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean check(PermissionSet source) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public MapCodec<AlwaysPass> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionCheck$AlwaysPass.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */