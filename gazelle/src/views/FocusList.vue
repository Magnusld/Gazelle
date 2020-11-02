<template>
  <div class="focusList">
    Fokuslista
    <vue-markdown>
      Litt *deilig* **markdown**
    </vue-markdown>
  </div>

</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import VueMarkdown from "vue-markdown";

@Component({
  components: {
    'vue-markdown': VueMarkdown
  }
})
export default class Home extends Vue {}
</script>
