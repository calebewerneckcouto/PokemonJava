// model/dto/PokeApiResponse.java
package com.cwcdev.pokemom.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PokeApiResponse {
    private Integer id;
    private String name;
    private Integer height;
    private Integer weight;
    private List<AbilityWrapper> abilities;
    private List<TypeWrapper> types;

    // Getters and Setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public Integer getHeight() { 
        return height; 
    }
    
    public void setHeight(Integer height) { 
        this.height = height; 
    }
    
    public Integer getWeight() { 
        return weight; 
    }
    
    public void setWeight(Integer weight) { 
        this.weight = weight; 
    }
    
    public List<AbilityWrapper> getAbilities() { 
        return abilities; 
    }
    
    public void setAbilities(List<AbilityWrapper> abilities) { 
        this.abilities = abilities; 
    }
    
    public List<TypeWrapper> getTypes() { 
        return types; 
    }
    
    public void setTypes(List<TypeWrapper> types) { 
        this.types = types; 
    }

    // Inner classes for nested JSON structure
    public static class AbilityWrapper {
        @JsonProperty("ability")
        private Ability ability;
        private Boolean isHidden;
        private Integer slot;

        public Ability getAbility() { 
            return ability; 
        }
        
        public void setAbility(Ability ability) { 
            this.ability = ability; 
        }
        
        public Boolean getIsHidden() { 
            return isHidden; 
        }
        
        public void setIsHidden(Boolean isHidden) { 
            this.isHidden = isHidden; 
        }
        
        public Integer getSlot() { 
            return slot; 
        }
        
        public void setSlot(Integer slot) { 
            this.slot = slot; 
        }
    }

    public static class Ability {
        private String name;
        private String url;

        public String getName() { 
            return name; 
        }
        
        public void setName(String name) { 
            this.name = name; 
        }
        
        public String getUrl() { 
            return url; 
        }
        
        public void setUrl(String url) { 
            this.url = url; 
        }
    }

    public static class TypeWrapper {
        @JsonProperty("type")
        private Type type;
        private Integer slot;

        public Type getType() { 
            return type; 
        }
        
        public void setType(Type type) { 
            this.type = type; 
        }
        
        public Integer getSlot() { 
            return slot; 
        }
        
        public void setSlot(Integer slot) { 
            this.slot = slot; 
        }
    }

    public static class Type {
        private String name;
        private String url;

        public String getName() { 
            return name; 
        }
        
        public void setName(String name) { 
            this.name = name; 
        }
        
        public String getUrl() { 
            return url; 
        }
        
        public void setUrl(String url) { 
            this.url = url; 
        }
    }
}