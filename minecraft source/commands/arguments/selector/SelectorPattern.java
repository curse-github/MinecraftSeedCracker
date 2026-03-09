/*    */ package net.minecraft.commands.arguments.selector;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ public final class SelectorPattern extends Record {
/*    */   private final String pattern;
/*    */   
/*  8 */   public SelectorPattern(String pattern, EntitySelector resolved) { this.pattern = pattern; this.resolved = resolved; } private final EntitySelector resolved; public String pattern() { return this.pattern; } public EntitySelector resolved() { return this.resolved; }
/*  9 */   public static final Codec<SelectorPattern> CODEC = Codec.STRING.comapFlatMap(SelectorPattern::parse, SelectorPattern::pattern);
/*    */   
/*    */   public static DataResult<SelectorPattern> parse(String pattern) {
/*    */     try {
/* 13 */       EntitySelectorParser parser = new EntitySelectorParser(new StringReader(pattern), true);
/* 14 */       return DataResult.success(new SelectorPattern(pattern, parser.parse()));
/* 15 */     } catch (CommandSyntaxException ex) {
/* 16 */       return DataResult.error(() -> "Invalid selector component: " + pattern + ": " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean equals(Object obj) { if (obj instanceof SelectorPattern) { SelectorPattern selector = (SelectorPattern)obj; if (this.pattern.equals(selector.pattern)); }  return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public int hashCode() { return this.pattern.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public String toString() { return this.pattern; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\selector\SelectorPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */