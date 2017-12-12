import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('elements/search-bar', 'Integration | Component | elements/search bar', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{elements/search-bar}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#elements/search-bar}}
      template block text
    {{/elements/search-bar}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
