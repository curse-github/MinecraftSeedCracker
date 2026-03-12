/*    */ package net.minecraft.util;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class IdentifierPattern {
/* 12 */   public static final Codec<IdentifierPattern> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.PATTERN
/* 13 */         .optionalFieldOf("namespace").forGetter(()), ExtraCodecs.PATTERN
/* 14 */         .optionalFieldOf("path").forGetter(()))
/* 15 */       .apply(i, IdentifierPattern::new));
/*    */   
/*    */   private final Optional<Pattern> namespacePattern;
/*    */   private final Predicate<String> namespacePredicate;
/*    */   private final Optional<Pattern> pathPattern;
/*    */   private final Predicate<String> pathPredicate;
/*    */   private final Predicate<Identifier> locationPredicate;
/*    */   
/*    */   private IdentifierPattern(Optional<Pattern> namespacePattern, Optional<Pattern> pathPattern) {
/* 24 */     this.namespacePattern = namespacePattern;
/* 25 */     this.namespacePredicate = (Predicate)namespacePattern.map(Pattern::asPredicate).orElse(r -> true);
/* 26 */     this.pathPattern = pathPattern;
/* 27 */     this.pathPredicate = (Predicate)pathPattern.map(Pattern::asPredicate).orElse(r -> true);
/* 28 */     this.locationPredicate = (location -> (this.namespacePredicate.test(location.getNamespace()) && this.pathPredicate.test(location.getPath())));
/*    */   }
/*    */ 
/*    */   
/* 32 */   public Predicate<String> namespacePredicate() { return this.namespacePredicate; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Predicate<String> pathPredicate() { return this.pathPredicate; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public Predicate<Identifier> locationPredicate() { return this.locationPredicate; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\IdentifierPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */