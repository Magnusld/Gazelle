<template>
  <div class="errorPage">
    <md-card>
      <md-card-media-cover class="md-solid">
        <md-card-media>
          <img
            src="../assets/images/ErrorImg.jpg"
            alt="ErrorImg"
            class="image"
          />
        </md-card-media>
        <md-card-area class="textArea">
          <md-card-header>
            <span class="imgText md-title">
              Error 404 Page not found
            </span>
            <span class="md-subhead">
              Det ser ut til at den siden du ser etter har blitt spist :(
            </span>
          </md-card-header>
        </md-card-area>
      </md-card-media-cover>
    </md-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class ErrorPage extends Vue {}
</script>

<style scoped lang="scss">
.errorPage {
  padding-top: 10px;
  display: flex;
  justify-content: center;
}

.imgText {
  font-size: 50px;
}

.image {
  height: auto;
  max-width: 575px;
}

.md-card {
  margin: 4px;
  display: inline-block;
  vertical-align: top;
}
</style>
