/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public static enum TriState
/*    */   implements StringRepresentable {
/*  9 */   TRUE("true"),
/* 10 */   FALSE("false"),
/* 11 */   DEFAULT("default");
/*    */   
/*    */   public static final Codec<TriState> CODEC;
/*    */   private final String name;
/*    */   
/*    */   static  {
/* 17 */     CODEC = Codec.either(Codec.BOOL, StringRepresentable.fromEnum(TriState::values)).xmap(either -> 
/* 18 */         (TriState)either.map(TriState::from, Function.identity()), triState -> {
/* 19 */           switch (triState.ordinal()) { default: throw new MatchException(null, null);case 2: case 0: case 1: break; }  return 
/*    */ 
/*    */             
/* 22 */             Either.left(Boolean.valueOf(false));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   TriState(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static TriState from(boolean value) { return value ? TRUE : FALSE; }
/*    */ 
/*    */   
/*    */   public boolean toBoolean(boolean defaultValue) {
/* 37 */     switch (ordinal()) { case 0: case 1:  }  return 
/*    */ 
/*    */       
/* 40 */       defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\TriState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */