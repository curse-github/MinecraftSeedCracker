/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class BooleanProperty extends Property<Boolean> {
/*  7 */   private static final List<Boolean> VALUES = List.of(Boolean.valueOf(true), Boolean.valueOf(false));
/*    */   
/*    */   private static final int TRUE_INDEX = 0;
/*    */   private static final int FALSE_INDEX = 1;
/*    */   
/* 12 */   private BooleanProperty(String name) { super(name, Boolean.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public List<Boolean> getPossibleValues() { return VALUES; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static BooleanProperty create(String name) { return new BooleanProperty(name); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Boolean> getValue(String name) {
/* 26 */     switch (name) { case "true": case "false":  }  return 
/*    */ 
/*    */       
/* 29 */       Optional.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public String getName(Boolean value) { return value.toString(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getInternalIndex(Boolean value) { return value.booleanValue() ? 0 : 1; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\BooleanProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */