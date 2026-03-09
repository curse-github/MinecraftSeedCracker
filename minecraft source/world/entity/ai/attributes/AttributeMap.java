/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ import com.google.common.collect.Multimap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class AttributeMap {
/*     */   private final Map<Holder<Attribute>, AttributeInstance> attributes;
/*     */   private final Set<AttributeInstance> attributesToSync;
/*     */   
/*     */   public AttributeMap(AttributeSupplier supplier) {
/*  18 */     this.attributes = new Object2ObjectOpenHashMap();
/*  19 */     this.attributesToSync = new ObjectOpenHashSet();
/*  20 */     this.attributesToUpdate = new ObjectOpenHashSet();
/*     */ 
/*     */ 
/*     */     
/*  24 */     this.supplier = supplier;
/*     */   }
/*     */   private final Set<AttributeInstance> attributesToUpdate; private final AttributeSupplier supplier;
/*     */   private void onAttributeModified(AttributeInstance attributeInstance) {
/*  28 */     this.attributesToUpdate.add(attributeInstance);
/*  29 */     if (((Attribute)attributeInstance.getAttribute().value()).isClientSyncable()) {
/*  30 */       this.attributesToSync.add(attributeInstance);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  35 */   public Set<AttributeInstance> getAttributesToSync() { return this.attributesToSync; }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public Set<AttributeInstance> getAttributesToUpdate() { return this.attributesToUpdate; }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public Collection<AttributeInstance> getSyncableAttributes() { return (Collection)this.attributes.values().stream().filter(instance -> ((Attribute)instance.getAttribute().value()).isClientSyncable()).collect(Collectors.toList()); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public AttributeInstance getInstance(Holder<Attribute> attribute) { return (AttributeInstance)this.attributes.computeIfAbsent(attribute, key -> this.supplier.createInstance(this::onAttributeModified, key)); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public boolean hasAttribute(Holder<Attribute> attribute) { return (this.attributes.get(attribute) != null || this.supplier.hasAttribute(attribute)); }
/*     */ 
/*     */   
/*     */   public boolean hasModifier(Holder<Attribute> attribute, Identifier id) {
/*  55 */     AttributeInstance attributeInstance = (AttributeInstance)this.attributes.get(attribute);
/*  56 */     return (attributeInstance != null) ? ((attributeInstance.getModifier(id) != null)) : this.supplier.hasModifier(attribute, id);
/*     */   }
/*     */   
/*     */   public double getValue(Holder<Attribute> attribute) {
/*  60 */     AttributeInstance ownAttribute = (AttributeInstance)this.attributes.get(attribute);
/*  61 */     return (ownAttribute != null) ? ownAttribute.getValue() : this.supplier.getValue(attribute);
/*     */   }
/*     */   
/*     */   public double getBaseValue(Holder<Attribute> attribute) {
/*  65 */     AttributeInstance ownAttribute = (AttributeInstance)this.attributes.get(attribute);
/*  66 */     return (ownAttribute != null) ? ownAttribute.getBaseValue() : this.supplier.getBaseValue(attribute);
/*     */   }
/*     */   
/*     */   public double getModifierValue(Holder<Attribute> attribute, Identifier id) {
/*  70 */     AttributeInstance attributeInstance = (AttributeInstance)this.attributes.get(attribute);
/*  71 */     return (attributeInstance != null) ? attributeInstance.getModifier(id).amount() : this.supplier.getModifierValue(attribute, id);
/*     */   }
/*     */   
/*     */   public void addTransientAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
/*  75 */     modifiers.forEach((attribute, attributeModifier) -> {
/*  76 */           AttributeInstance instance = getInstance(attribute);
/*     */           
/*  78 */           if (instance != null) {
/*  79 */             instance.removeModifier(attributeModifier.id());
/*  80 */             instance.addTransientModifier(attributeModifier);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void removeAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
/*  86 */     modifiers.asMap().forEach((attribute, attributeModifiers) -> {
/*  87 */           AttributeInstance instance = (AttributeInstance)this.attributes.get(attribute);
/*     */           
/*  89 */           if (instance != null) {
/*  90 */             attributeModifiers.forEach(());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignAllValues(AttributeMap other) {
/*  96 */     other.attributes.values().forEach(otherInstance -> {
/*  97 */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/*  98 */           if (selfInstance != null) {
/*  99 */             selfInstance.replaceFrom(otherInstance);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignBaseValues(AttributeMap other) {
/* 105 */     other.attributes.values().forEach(otherInstance -> {
/* 106 */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/* 107 */           if (selfInstance != null) {
/* 108 */             selfInstance.setBaseValue(otherInstance.getBaseValue());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignPermanentModifiers(AttributeMap other) {
/* 114 */     other.attributes.values().forEach(otherInstance -> {
/* 115 */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/* 116 */           if (selfInstance != null) {
/* 117 */             selfInstance.addPermanentModifiers(otherInstance.getPermanentModifiers());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean resetBaseValue(Holder<Attribute> attribute) {
/* 123 */     if (!this.supplier.hasAttribute(attribute)) {
/* 124 */       return false;
/*     */     }
/* 126 */     AttributeInstance instance = (AttributeInstance)this.attributes.get(attribute);
/* 127 */     if (instance != null) {
/* 128 */       instance.setBaseValue(this.supplier.getBaseValue(attribute));
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public List<AttributeInstance.Packed> pack() {
/* 134 */     List<AttributeInstance.Packed> result = new ArrayList<AttributeInstance.Packed>(this.attributes.values().size());
/* 135 */     for (AttributeInstance attribute : this.attributes.values()) {
/* 136 */       result.add(attribute.pack());
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */   
/*     */   public void apply(List<AttributeInstance.Packed> packedAttributes) {
/* 142 */     for (AttributeInstance.Packed packedAttribute : packedAttributes) {
/* 143 */       AttributeInstance instance = getInstance(packedAttribute.attribute());
/* 144 */       if (instance != null)
/* 145 */         instance.apply(packedAttribute); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */